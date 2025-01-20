package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	private final UserRepository userRepository;
	
	public ReviewService(ReviewRepository reviewRepository, HouseRepository houseRepository, UserRepository userRepository) {
		this.reviewRepository = reviewRepository;
		this.userRepository = userRepository;
		this.houseRepository = houseRepository;
	}
	
	@Transactional
	public void create(ReviewForm reviewForm) {
		Review review = new Review();
		User user = userRepository.getReferenceById(reviewForm.getUserId());
		House house = houseRepository.getReferenceById(reviewForm.getHouseId());
		
		review.setHouse(house);
		review.setUser(user);
		review.setEvaluation(reviewForm.getEvaluation());
		review.setComment(reviewForm.getComment());
		
		reviewRepository.save(review);
	}
	
	@Transactional
	public void update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
		User user = userRepository.getReferenceById(reviewEditForm.getUserId());
		House house = houseRepository.getReferenceById(reviewEditForm.getHouseId());
		
		review.setHouse(house);
		review.setUser(user);
		review.setEvaluation(reviewEditForm.getEvaluation());
		review.setComment(reviewEditForm.getComment());
		
		reviewRepository.save(review);
	}
}
