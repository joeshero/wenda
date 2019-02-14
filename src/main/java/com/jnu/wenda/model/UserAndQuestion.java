package com.jnu.wenda.model;

import com.jnu.wenda.service.QuestionService;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe
 */
@Data
public class UserAndQuestion {

    private Question question;
    private User user;

}
