/**
 * Copyright (C) 2013-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.series.db.srv.v2;

import org.n52.io.format.TvpDataCollection;
import org.n52.io.request.IoParameters;
import org.n52.io.request.RequestSimpleParameterSet;
import org.n52.io.response.OutputCollection;
import org.n52.io.response.v2.SeriesMetadataOutput;
import org.n52.sensorweb.spi.ParameterService;
import org.n52.sensorweb.spi.TimeseriesDataService;
import org.n52.series.db.srv.ServiceInfoAccess;

public class SeriesAccessService extends ServiceInfoAccess implements TimeseriesDataService, ParameterService<SeriesMetadataOutput> {

	@Override
	public OutputCollection<SeriesMetadataOutput> getExpandedParameters(IoParameters query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputCollection<SeriesMetadataOutput> getCondensedParameters(IoParameters query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputCollection<SeriesMetadataOutput> getParameters(String[] items) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputCollection<SeriesMetadataOutput> getParameters(String[] items, IoParameters query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SeriesMetadataOutput getParameter(String item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SeriesMetadataOutput getParameter(String item, IoParameters query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TvpDataCollection getTimeseriesData(RequestSimpleParameterSet parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}