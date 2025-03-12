package com.mycompany.matdongsan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {
    @NotBlank(message = "휴대폰 번호를 입력해주세요")
    private String phoneNum;
}
