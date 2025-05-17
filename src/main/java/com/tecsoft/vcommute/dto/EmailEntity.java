package com.tecsoft.vcommute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailEntity {

    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

}
