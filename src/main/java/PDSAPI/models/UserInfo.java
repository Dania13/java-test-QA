package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Информация о пользователе, создавшем или оформившем полис.
 * <p>Содержит данные о сотруднике страховой компании или агенте,
 * работающем с полисом.</p>
 */
@Setter
@Getter
public class UserInfo {

    /** Фамилия пользователя */
    private String lastName;

    /** Имя пользователя */
    private String name;

    /** Логин пользователя в системе */
    private String userName;

    /** Табельный номер сотрудника */
    private String personnelNumber;

    /** Адрес электронной почты пользователя */
    private String email;
}