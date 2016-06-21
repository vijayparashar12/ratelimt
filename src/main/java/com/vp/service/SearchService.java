package com.vp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vp.data.Hotel;
import com.vp.data.HotelDB;
import com.vp.data.SearchCriteria;
import com.vp.data.SearchCriteria.Sort;

/**
 * @author vijay
 *	This is a service implementation, this class query in memory hotelDB for give city, 
 *	it also sort the result in given sort option.
 */
@Service
public class SearchService {
    private HotelDB hotelDB;

    public SearchService() {
        hotelDB = HotelDB.getInstance();
    }

    public List<Hotel> search(SearchCriteria searchCriteria) {
        List<Hotel> cityHotels = hotelDB.getCityHotels(searchCriteria.getCity());
        int factor = searchCriteria.getSort() == Sort.ASC ? 1 : -1;
        cityHotels.sort((h1, h2) -> factor * Double.valueOf(h1.getRoom().getPrice()).compareTo(h2.getRoom().getPrice()));
        return cityHotels;
    }

}
