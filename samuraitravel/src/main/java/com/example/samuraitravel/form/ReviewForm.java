package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ReviewForm {
	
	@NotNull
	private Integer houseId;
	
	@NotNull
	private Integer userId;
	
	@NotNull(message = "評価を選択してください")
	private Integer evaluation;

	@NotBlank(message = "コメントを入力してください")
	private String  comment;
	
}
