/**
 * Copyright (C) 2013-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.series.api.v1.db.da.dao;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.sql.JoinType.LEFT_OUTER_JOIN;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.n52.io.IoParameters;
import org.n52.series.api.v1.db.da.DataAccessException;
import org.n52.series.api.v1.db.da.DbQuery;
import org.n52.series.api.v1.db.da.beans.FeatureEntity;
import org.n52.series.api.v1.db.da.beans.I18nFeatureEntity;
import org.n52.series.api.v1.db.da.beans.I18nOfferingEntity;
import org.n52.series.api.v1.db.da.beans.I18nProcedureEntity;
import org.n52.series.api.v1.db.da.beans.MergableBaseSeriesEntity;
import org.n52.series.api.v1.db.da.beans.SeriesEntity;

public class SeriesDao extends AbstractDao<SeriesEntity> {

    private static final String COLUMN_PKID = "pkid";

    public SeriesDao(Session session) {
        super(session);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SeriesEntity> find(String search, DbQuery query) {

        /*
         * Timeseries labels are constructed from labels of related feature
         * and phenomenon. Therefore we have to join both tables and search
         * for given pattern on any of the stored labels.
         */

        List<SeriesEntity> series = new ArrayList<SeriesEntity>();
        Criteria criteria = getDefaultCriteria("series");
        Criteria featureCriteria = criteria.createCriteria("feature", LEFT_OUTER_JOIN);

        if (hasTranslation(query, I18nFeatureEntity.class)) {
            featureCriteria = query.addLocaleTo(featureCriteria, I18nFeatureEntity.class);
        }
        featureCriteria.add(Restrictions.ilike("name", "%" + search + "%"));
        series.addAll(featureCriteria.list());
        
        // reset criteria
        criteria = getDefaultCriteria("series");
        Criteria procedureCriteria = criteria.createCriteria("procedure", LEFT_OUTER_JOIN);
        if (hasTranslation(query, I18nProcedureEntity.class)) {
            procedureCriteria = query.addLocaleTo(procedureCriteria, I18nProcedureEntity.class);
        }
        procedureCriteria.add(Restrictions.ilike("name", "%" + search + "%"));
        series.addAll(procedureCriteria.list());

        // reset criteria
        criteria = getDefaultCriteria("series");
        Criteria offeringCriteria = criteria.createCriteria("offering", LEFT_OUTER_JOIN);
        if (hasTranslation(query, I18nOfferingEntity.class)) {
            offeringCriteria = query.addLocaleTo(offeringCriteria, I18nOfferingEntity.class);
        }
        offeringCriteria.add(Restrictions.ilike("name", "%" + search + "%"));
        series.addAll(offeringCriteria.list());
        return series;
    }

    @Override
    public SeriesEntity getInstance(Long key) throws DataAccessException {
        return getInstance(key, DbQuery.createFrom(IoParameters.createDefaults()));
    }

    @Override
    public SeriesEntity getInstance(Long key, DbQuery query) throws DataAccessException {
        Criteria criteria = session.createCriteria(SeriesEntity.class)
                .add(eq("pkid", key));
        addMergeRoles(criteria, query);
        addIgnoreNonPublishedSeriesTo(criteria);
        return (SeriesEntity) getDefaultCriteria("series", query)
                .add(eq(COLUMN_PKID, key))
                .uniqueResult();
    }
    

    private void addMergeRoles(Criteria criteria, DbQuery query) {
        addMergeRoles(criteria, query, "");
    }

    private void addMergeRoles(Criteria criteria, DbQuery query, String alias) {
        String property = alias != null && !alias.isEmpty()
                ? alias + ".mergeRole"
                : "mergeRole";
        if (query.getParameters().containsParameter("merge_roles")) {
            Set<String> roles = query.getParameters().getOthers("merge_roles");
            Disjunction disjunction = Restrictions.disjunction();
            for (String role : roles) {
                disjunction.add(eq(property, role));
            }
            criteria.add(disjunction);
        }
    }

    @Override
    public List<SeriesEntity> getAllInstances() throws DataAccessException {
        return getAllInstances(DbQuery.createFrom(IoParameters.createDefaults()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SeriesEntity> getAllInstances(DbQuery query) throws DataAccessException {
        Criteria criteria = getDefaultCriteria("series");
        addIgnoreNonPublishedSeriesTo(criteria, "series");
        if ( !query.getParameters().containsParameter("merge_roles")) {
            query = DbQuery.createFrom(query.getParameters()
                    .extendWith("merge_roles", "master"));
        }
        addMergeRoles(criteria, query);
        Criteria criteria = getDefaultCriteria("series", query);
        criteria = query.addDetachedFilters("", criteria);
        query.addPagingTo(criteria);
        return (List<SeriesEntity>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<SeriesEntity> getInstancesWith(FeatureEntity feature) {
        Criteria criteria = getDefaultCriteria("series");
        criteria.add(eq("feature.pkid", feature.getPkid()));
        Criteria criteria = getDefaultCriteria("series")
                .createAlias("feature", "f")
                .add(Restrictions.eq("f.pkid", feature.getPkid()));
//        criteria.add(eq("feature.pkid", feature.getPkid()));
        return (List<SeriesEntity>) criteria.list();
    }

    @Override
    public int getCount() throws DataAccessException {
        Criteria criteria = session
                .createCriteria(MergableBaseSeriesEntity.class)
                .setProjection(Projections.rowCount());
        return criteria != null ? ((Long) criteria.uniqueResult()).intValue() : 0;
    }

    private Criteria addIgnoreNonPublishedSeriesTo(Criteria criteria) {
        return addIgnoreNonPublishedSeriesTo(criteria, null);
    }
    
    @Override
    protected String getDefaultAlias() {
        return "series";
    }

    @Override
    protected Class<?> getEntityClass() {
        return SeriesEntity.class;
    }

    @Override
    protected Criteria getDefaultCriteria(String alias, DbQuery query) {
        alias = alias != null ? alias : getDefaultAlias();
        Criteria criteria = session.createCriteria(getEntityClass(), alias)
                .createAlias("procedure", "p")
                .add(eq("p.reference", Boolean.FALSE));
        addIgnoreNonPublishedSeriesTo(criteria, alias);
        return criteria;
    }

    private Criteria addIgnoreNonPublishedSeriesTo(Criteria criteria, String alias) {
        alias = alias == null ? "" : alias + ".";
        criteria.add(Restrictions.and(
                Restrictions.and(
                        Restrictions.isNotNull(alias + "firstValue"),
                        Restrictions.isNotNull(alias + "lastValue")),
                        Restrictions.eq(alias + "published", true),
                        Restrictions.eqOrIsNull(alias + "deleted", false)));
        return criteria;
    }

}
