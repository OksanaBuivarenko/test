package com.fintech.database.service;

import com.fintech.database.dto.response.PageRs;

public interface FillDbService {

    PageRs<String> fillDb();
}