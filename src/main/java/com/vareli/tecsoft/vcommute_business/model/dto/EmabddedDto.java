package com.vareli.tecsoft.vcommute_business.model.dto;

import java.util.List;

import com.vareli.tecsoft.vcommute_business.model.Attendance;
import com.vareli.tecsoft.vcommute_business.model.CommuteHeader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmabddedDto {
    private double[] embeddingData;

	private List<CommuteHeader> commuteData;

	private List<Attendance> attendanceData;

    private Long openAttendanceId;

	private Long openCommuteHeaderId;

	private Long openCommuteDetailsId;

	private boolean isDocumentRequired;

	private boolean isPriceRequired;

	private boolean checkEmbedding;
}
