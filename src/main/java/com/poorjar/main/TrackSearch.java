package com.poorjar.main;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.poorjar.entities.Track;
import com.poorjar.hibernate.utils.HibernateUtil;

/**
 * Main application for searching track table.
 */
public class TrackSearch
{

    private static void doIndex() throws InterruptedException
    {
        Session session = HibernateUtil.getSession();

        FullTextSession fullTextSession = Search.getFullTextSession(session);
        fullTextSession.createIndexer().startAndWait();

        fullTextSession.close();
    }

    @SuppressWarnings("unchecked")
    private static List<Track> search(String queryString)
    {
        Session session = HibernateUtil.getSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Track.class).get();
        org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().fuzzy().onFields("Name", "Composer").matching(queryString)
                .createQuery();

        // wrap Lucene query in a javax.persistence.Query
        org.hibernate.Query fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Track.class);

        List<Track> trackList = fullTextQuery.list();

        fullTextSession.close();

        return trackList;
    }

    @SuppressWarnings("unchecked")
    private static void displayTrackTableData()
    {
        Session session = null;

        try
        {
            session = HibernateUtil.getSession();

            // Fetching saved data
            List<Track> trackList = session.createQuery("from Track").list();

            for (Track track : trackList)
            {
                System.out.println(track);
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
        System.out.println("\n\n******Data stored in Track table******\n");
        displayTrackTableData();

        // Create an initial Lucene index for the data already present in the database
        doIndex();

        Scanner scanner = new Scanner(System.in);
        try
        {
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

                List<Track> result = search(consoleInput);
                System.out.println("\n\n>>>>>>Record found for '" + consoleInput + "'");

                for (Track track : result)
                {
                    System.out.println(track);
                }
            }
        } finally
        {
            scanner.close();
        }
    }
}