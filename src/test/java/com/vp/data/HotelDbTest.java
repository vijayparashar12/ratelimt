package com.vp.data;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class HotelDbTest {

    @Test
    public void test() {
        HotelDB instance = HotelDB.getInstance();
        assertNotNull(instance.getHotel(1));
        assertEquals(instance.getHotel(1).getRoom().getPrice() , 1000d,0.001);
        List<Hotel> cityHotels = instance.getCityHotels("Bangkok");
        assertEquals(cityHotels.size(),6);
    }

}
