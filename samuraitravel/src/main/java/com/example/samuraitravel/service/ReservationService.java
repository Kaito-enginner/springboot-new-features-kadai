package com.example.samuraitravel.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Reservation;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReservationRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class ReservationService {
	private final HouseRepository houseRepository;
	private final ReservationRepository reservationRepository;
	private final UserRepository userRepository;
	
	public ReservationService(HouseRepository houseRepository, ReservationRepository reservationRepository, UserRepository userRepository) {
		this.houseRepository = houseRepository;
		this.reservationRepository = reservationRepository;
		this.userRepository = userRepository;
	}
	
	@Transactional
	public void create(Map<String, String>paymentIntentObject) {
		Reservation reservation = new Reservation();
		
		Integer houseId = Integer.valueOf(paymentIntentObject.get("houseId"));
		Integer userId = Integer.valueOf(paymentIntentObject.get("userId"));
		
		House house = houseRepository.getReferenceById(houseId);
		User user = userRepository.getReferenceById(userId);
		LocalDate CheckinDate = LocalDate.parse(paymentIntentObject.get("checkinDate"));
		LocalDate CheckoutDate = LocalDate.parse(paymentIntentObject.get("checkoutDate"));
		Integer numberOfPeople = Integer.valueOf(paymentIntentObject.get("numberOfPeople"));
		Integer amount = Integer.valueOf(paymentIntentObject.get("amount"));
		
		
		reservation.setHouse(house);
		reservation.setUser(user);
		reservation.setCheckinDate(CheckinDate);
		reservation.setCheckoutDate(CheckoutDate);
		reservation.setNumberOfPeople(numberOfPeople);
		reservation.setAmount(amount);
		
		reservationRepository.save(reservation);
		
	}
	
	// 宿泊人数が定員以下かどうかをチェックする
	public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {
		return numberOfPeople <= capacity;
	}
	
	// 宿泊料金を計算する
	public Integer caluculateAmount(LocalDate checkinDate, LocalDate checkoutDate, Integer price) {
		long numberOfNights = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
		int amount = price * (int)numberOfNights;
		
		return amount;
	}
}
