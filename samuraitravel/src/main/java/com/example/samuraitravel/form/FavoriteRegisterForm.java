package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FavoriteRegisterForm {
	@NotNull
	private Integer houseId;
	
	@NotNull
	private Integer userId;
}
