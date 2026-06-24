package cards.api.fintech.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "account-service")
public interface AccountClient {
    @GetMapping("/api/v1/accounts/{id}")
    AccountModel getAccountById(@PathVariable Long id);
}
