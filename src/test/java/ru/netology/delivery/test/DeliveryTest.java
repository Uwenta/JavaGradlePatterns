package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    LocalDate today = LocalDate.now();

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("input[placeholder='Город']").setValue(validUser.getCity());
        $("input[placeholder='Дата встречи']").sendKeys(Keys.CONTROL + "a");
        $("input[placeholder='Дата встречи']").sendKeys(Keys.DELETE);
        $("input[placeholder='Дата встречи']").setValue(firstMeetingDate);
        $("input[name='name']").setValue(validUser.getName());
        $("input[name='phone']").setValue(validUser.getPhone());
        $("label[data-test-id='agreement']").click();
        $x("//span[contains(text(),'Запланировать')]//ancestor::button").click();
        $x("//*[contains(text(),'Успешно')]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15))
                .shouldBe(visible);

        $("input[placeholder='Дата встречи']").sendKeys(Keys.CONTROL + "a");
        $("input[placeholder='Дата встречи']").sendKeys(Keys.DELETE);
        $("input[placeholder='Дата встречи']").setValue(secondMeetingDate);
        $x("//span[contains(text(),'Запланировать')]//ancestor::button").click();
        $("div[data-test-id='replan-notification']")
                .shouldBe(visible, Duration.ofSeconds(15));
        $x("//span[contains(text(),'Перепланировать')]//ancestor::button").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15))
                .shouldBe(visible);
        $x("//*[contains(text(),'Успешно')]").shouldBe(visible, Duration.ofSeconds(15));

    }

    @Test
    void shouldSubmitTheForm() {
        String strDate = DataGenerator.generateDate(5);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("input[placeholder='Город']").setValue(validUser.getCity());
        $("input[placeholder='Дата встречи']").sendKeys(Keys.CONTROL + "a");
        $("input[placeholder='Дата встречи']").sendKeys(Keys.DELETE);
        $("input[placeholder='Дата встречи']").setValue(strDate);
        $("input[name='name']").setValue(validUser.getName());
        $("input[name='phone']").setValue(validUser.getPhone());
        $("label[data-test-id='agreement']").click();
        $x("//span[contains(text(),'Запланировать')]//ancestor::button").click();
        $x("//*[contains(text(),'Успешно')]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + strDate), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    void shouldSubmitTheFormWithChoiceCalendarWeekLater() {
        LocalDate date = today.plusDays(7);
        int days = date.getDayOfMonth();
        int year = date.getYear();
        int month = date.getMonthValue();
        String strDate = DataGenerator.generateDate(7);
        var validUser = DataGenerator.Registration.generateUser("ru");

        char[] beginCity = {validUser.getCity().charAt(0), validUser.getCity().charAt(1)};
        $("input[placeholder='Город']").setValue(String.copyValueOf(beginCity));
        $x("//span[(text()='" + validUser.getCity() + "')]").click();

        $(".calendar-input__custom-control").click();
        if (month == today.getMonthValue() && year == today.getYear()) {
            $x("//td[contains(text(), '" + days + "')]").click();
        } else {
            $(".calendar__arrow_direction_right[data-step='1']").click();
            $x("//td[contains(text(), '" + days + "')]").click();
        }

        $("input[name='name']").setValue(validUser.getName());
        $("input[name='phone']").setValue(validUser.getPhone());
        $("label[data-test-id='agreement']").click();
        $x("//span[contains(text(),'Запланировать')]//ancestor::button").click();
        $x("//*[contains(text(),'Успешно')]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + strDate), Duration.ofSeconds(15))
                .shouldBe(visible);

    }

}
