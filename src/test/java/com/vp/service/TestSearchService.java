package com.vp.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.vp.data.Hotel;
import com.vp.data.SearchCriteria;

public class TestSearchService {

    @Test
    public void test() {
        SearchService searchService = new SearchService();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCity("Bangkok");
        List<Hotel> search = searchService.search(searchCriteria);
        assertEquals(7, search.size());
    }

}
