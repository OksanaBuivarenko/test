package com.fintech.database.service;

import java.util.List;

public interface HttpService<T> {

    List<T> getListByApi();
}