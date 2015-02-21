package com.poorjar.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * The persistent class for the vod_asset table.
 */
@Entity
@Indexed
@Table(name = "vod_asset")
public class VodAsset
{
    private Long assetId;
    private String title;
    private String description;

    public VodAsset()
    {
    }

    /**
     * @param assetId The unique asset id.
     * @param title The title of the asset.
     * @param description The description of the asset.
     */
    public VodAsset(Long assetId, String title, String description)
    {
        super();
        this.assetId = assetId;
        this.title = title;
        this.description = description;
    }

    /**
     * @return the assetId
     */
    @Id
    @Column(name = "asset_id", unique = true, nullable = false)
    public final Long getAssetId()
    {
        return assetId;
    }

    /**
     * @param assetId the assetId to set
     */
    public final void setAssetId(Long assetId)
    {
        this.assetId = assetId;
    }

    /**
     * @return the title
     */
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    public final String getTitle()
    {
        return title;
    }

    /**
     * @param title the title to set
     */
    public final void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return the description
     */
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    public final String getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public final void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder("assetId: ");
        stringBuilder.append(this.getAssetId());
        stringBuilder.append(" | title:");
        stringBuilder.append(this.getTitle());
        stringBuilder.append(" | description:");
        stringBuilder.append(this.getDescription());

        return stringBuilder.toString();
    }
}
