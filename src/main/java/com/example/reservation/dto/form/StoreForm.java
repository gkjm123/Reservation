package com.example.reservation.dto.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreForm {

  @NotBlank(message = "상점 이름을 입력하세요.")
  private String storeName;

  @NotBlank(message = "상점 위치를 입력하세요.")
  private String location;

  @NotBlank(message = "상점 설명을 입력하세요.")
  private String description;

  //30분마다 시간을 잘라서 한타임당 몇건의 예약을 받을수 있는지 설정가능(추가 구현)
  @Min(value = 1, message = "타임당 예약가능한 테이블수를 입력하세요.")
  private Long tablePerTime;

  //상점 문여는 시간(추가 구현)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  private LocalTime startTime;

  //상점 문닫는 시간(추가 구현)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  private LocalTime endTime;
}