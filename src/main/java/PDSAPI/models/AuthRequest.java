package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Запрос аутентификации пользователя.
 * <p>Содержит учетные данные для входа в систему страхования.</p>
 */
@Setter
@Getter
@Builder
public class AuthRequest {
    /** Логин пользователя */
    private String login;
    /** Пароль пользователя */
    private String password;
}
