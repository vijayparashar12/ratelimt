package com.vp.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.vp.data.Hotel;
import com.vp.data.HotelDB;
import com.vp.data.SearchCriteria;
import com.vp.data.SearchCriteria.Sort;
import com.vp.data.exception.InvalidParameterException;

/**
 * @author vijay 
 * This is a service implementation, this class query in memory
 * hotelDB for give city, it also sort the result in given sort option.
 */
@Service
public class SearchService {
	private HotelDB hotelDB;
	private static Log log = LogFactory.getLog(SearchService.class);

	public SearchService() {
		hotelDB = HotelDB.getInstance();
	}

	public List<Hotel> search(SearchCriteria searchCriteria) {
		List<Hotel> cityHotels = null;
		try {
			cityHotels = hotelDB.getCityHotels(searchCriteria.getCity());
			if (cityHotels != null) {
				int factor = searchCriteria.getSort() == Sort.ASC ? 1 : -1;
				cityHotels.sort(
						(h1, h2) -> factor * Double.valueOf(h1.getRoom().getPrice()).compareTo(h2.getRoom().getPrice()));
			}
		} catch (InvalidParameterException e) {
			log.error("City is Null");
		}
		
		return cityHotels;
	}

}
