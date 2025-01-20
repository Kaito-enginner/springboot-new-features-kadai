package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
@RequestMapping("/houses/{id}")
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	private final ReviewService reviewService;
	
	public ReviewController(ReviewRepository reviewRepository, HouseRepository houseRepository, ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;
		this.reviewService = reviewService;
	}
	
	@GetMapping("/review")
	public String index(@PathVariable(name = "id") Integer id,  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model,
						@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) 
	{
		House house = houseRepository.getReferenceById(id);
		Page<Review> reviewpage = reviewRepository.findByHouse(house, pageable);
		
		if(userDetailsImpl != null) {
			User user = userDetailsImpl.getUser();
			Integer userId = user.getId();
			model.addAttribute("userId", userId);
		}
		
		
		model.addAttribute("house", house);
		model.addAttribute("reviewpage",reviewpage);
		
		return "review/index";
	}
	
	@PostMapping("/{reviewpage}/delete")
	public String delete(@PathVariable(name = "reviewpage") Integer reviewId, RedirectAttributes redirectAttributes) {
		reviewRepository.deleteById(reviewId);
		
        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
		
		return "redirect:/houses/{id}/review";
	}
	
	@GetMapping("/register")
	public String register(@PathVariable(name = "id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		House house = houseRepository.getReferenceById(id);
		
		if(userDetailsImpl != null) {
			User user = userDetailsImpl.getUser();
			Integer userId = user.getId();
			model.addAttribute("userId", userId);
		}
		
		model.addAttribute("house", house);
		model.addAttribute("reviewForm", new ReviewForm());
		
		return "review/register";
	}
	
	@PostMapping("/create")
	public String create(@PathVariable(name = "id") Integer id,
						 @ModelAttribute @Validated ReviewForm reviewForm,
						 BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) 
	{		
		if(bindingResult.hasErrors()) {
			Integer userId =reviewForm.getUserId();
			model.addAttribute("userId", userId);
			House house = houseRepository.getReferenceById(id);
			model.addAttribute("house", house);
			
			return "review/register";
		}
		
		reviewService.create(reviewForm);
        redirectAttributes.addFlashAttribute("successMessage", "レビューの投稿を完了しました。");    

		
		return "redirect:/houses/{id}/review";
	}
	
	@GetMapping("/{user}/{reviewpage}/edit")
	public String edit(@PathVariable(name = "id") Integer id, @PathVariable(name = "user") Integer userId,
					   @PathVariable(name = "reviewpage") Integer reviewId, Model model) 
	{
		Review review = reviewRepository.getReferenceById(reviewId);
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), id, userId, review.getEvaluation(),review.getComment());
		House house = houseRepository.getReferenceById(id);
		
		model.addAttribute("house", house);
		model.addAttribute("reviewEditForm", reviewEditForm);
		
		return "review/edit";
	}	
	
	@PostMapping("/update")
	public String update(@PathVariable(name = "id") Integer id,
						 @ModelAttribute @Validated ReviewEditForm reviewEditForm,
						 BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) 
	{		
		if(bindingResult.hasErrors()) {
			
			House house = houseRepository.getReferenceById(id);
			model.addAttribute("house", house);
			model.addAttribute("reviewEditForm", reviewEditForm);
			
			return "review/edit";
		}
		
		reviewService.update(reviewEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");    

		
		return "redirect:/houses/{id}/review";
	}
	
}
