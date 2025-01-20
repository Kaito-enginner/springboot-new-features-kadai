package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.FavoriteRegisterForm;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

@Controller
public class FavoriteController {
	private final FavoriteRepository favoriterepository;
	private final FavoriteService favoriteService;
	
	public FavoriteController(FavoriteRepository favoriterepository, FavoriteService favoriteService) {
		this.favoriterepository = favoriterepository;
		this.favoriteService = favoriteService;
	}
	
	@GetMapping("/favorite")
	public String favorite(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
						   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		User user = userDetailsImpl.getUser();
		Page<Favorite> favoritePage = favoriterepository.findByUser(user, pageable);
		
		model.addAttribute("favoritePage", favoritePage);
		
		return "favorite/index";
	}
	
	@PostMapping("/favorite/create")
	public String create(FavoriteRegisterForm favoriteRegisterForm,  RedirectAttributes redirectAttributes) {
		
		favoriteService.create(favoriteRegisterForm);
		
        redirectAttributes.addFlashAttribute("successMessage", "お気に入りに登録しました。");    
		return "redirect:/favorite";
	}
	
	@PostMapping("/favorite/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		favoriterepository.deleteById(id);
		
		redirectAttributes.addFlashAttribute("successMessage", "お気に入りに削除しました。"); 
		 return "redirect:/favorite";
	}
}
