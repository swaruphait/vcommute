package com.tecsoft.vcommute.dto;

import java.util.List;

import com.tecsoft.vcommute.model.AttendanceData;
import com.tecsoft.vcommute.model.TravelDataHeader;

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

	private List<TravelDataHeader> commuteData;

	private List<AttendanceData> attendanceData;

    private Long openAttendanceId;

	private Long openCommuteHeaderId;

	private Long openCommuteDetailsId;

	private boolean isDocumentRequired;

	private boolean isPriceRequired;

	private boolean checkEmbedding;

	private boolean faceAttendance;

    private boolean normalAttendance;

	private boolean faceUploaded;
}
