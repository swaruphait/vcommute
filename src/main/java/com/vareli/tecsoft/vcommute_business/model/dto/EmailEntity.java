package com.vareli.tecsoft.vcommute_business.model.dto;

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
public class EmailEntity {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment; 
}
