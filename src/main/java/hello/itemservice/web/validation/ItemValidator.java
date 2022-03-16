package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        //item == clazz
        //item == subItem(자식클래스) -> isAssignableFrom은 자식클래스까지 모두 체크한다.
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        if(!StringUtils.hasText(item.getItemName())) {
            errors.rejectValue("itemName", "required");
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
            if (item.getQuantity() == null || item.getQuantity() >= 9999) {
                errors.rejectValue("quantity", "max", new Object[]{9999}, null);
            }

            // 특정 필드가 아닌 복합 룰 검증
            if (item.getPrice() != null && item.getQuantity() != null) {
                int resultPrice = item.getPrice() * item.getQuantity();
                if (resultPrice < 10000) {
                    // 특정 필드가 아닌 글로벌 오류이기 때문에 new ObjectError를 사용한다.
                    errors.reject("totalPriceMin", new Object[]{1000, resultPrice}, null);
                }
            }
        }
    }
}
