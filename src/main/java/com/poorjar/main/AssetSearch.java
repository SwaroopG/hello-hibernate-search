package com.poorjar.main;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.poorjar.entities.VodAsset;
import com.poorjar.hibernate.utils.HibernateUtil;

/**
 * Main application
 */
public class AssetSearch
{

    private static void doIndex() throws InterruptedException
    {
        Session session = HibernateUtil.getSession();

        FullTextSession fullTextSession = Search.getFullTextSession(session);
        fullTextSession.createIndexer().startAndWait();

        fullTextSession.close();
    }

    private static List<VodAsset> search(String queryString)
    {
        Session session = HibernateUtil.getSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(VodAsset.class).get();
        org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().fuzzy().onFields("title", "description").matching(queryString)
                .createQuery();

        // wrap Lucene query in a javax.persistence.Query
        org.hibernate.Query fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, VodAsset.class);

        List<VodAsset> assetList = fullTextQuery.list();

        fullTextSession.close();

        return assetList;
    }

    private static void displayVodAssetTableData()
    {
        Session session = null;

        try
        {
            session = HibernateUtil.getSession();

            // Fetching saved data
            List<VodAsset> assetList = session.createQuery("from VodAsset").list();

            for (VodAsset asset : assetList)
            {
                System.out.println(asset);
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        } finally
        {
            if (session != null)
            {
                session.close();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("\n\n******Data stored in VodAsset table******\n");
        displayVodAssetTableData();

        // Create an initial Lucene index for the data already present in the database
        doIndex();

        Scanner scanner = new Scanner(System.in);
        String consoleInput = null;

        while (true)
        {
            // Prompt the user to enter query string
            System.out.print("\n\nEnter search key (To exit type 'X')");
            consoleInput = scanner.nextLine();

            if ("X".equalsIgnoreCase(consoleInput))
            {
                System.out.println("End");
                System.exit(0);
            }

            try
            {
                List<VodAsset> result = search(consoleInput);
                System.out.println("\n\n>>>>>>Record found for '" + consoleInput + "'");

                for (VodAsset asset : result)
                {
                    System.out.println(asset);
                }
            } catch (Exception e)
            {
                System.out.println("Caught an exception!\n" + e.getCause());
                continue;
            }
        }
    }
}