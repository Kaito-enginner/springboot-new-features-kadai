package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.FavoriteRegisterForm;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final HouseRepository houseRepository;
	private final UserRepository userRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, HouseRepository houseRepository) {
		this.favoriteRepository = favoriteRepository;
		this.userRepository = userRepository;
		this.houseRepository = houseRepository;
	}
	
	@Transactional
	public void create(FavoriteRegisterForm favoriteRegisterForm) {
		Favorite favorite = new Favorite();
		User user = userRepository.getReferenceById(favoriteRegisterForm.getUserId());
		House house = houseRepository.getReferenceById(favoriteRegisterForm.getHouseId());
		
		favorite.setHouse(house);
		favorite.setUser(user);
		
		favoriteRepository.save(favorite);
	}
}
