package org.myprojects.simple_shop_app.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.myprojects.simple_shop_app.auth.service.TokenManagerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumerService {
    private final TokenManagerService tokenManagerService;

    @KafkaListener(
            topics = "${spring.kafka.topic.revoked-tokens.name}",
            concurrency = "3"
    )
    void listenRevokedTokens(final ConsumerRecord<String, String> record) {
        log.info("[KafkaConsumerService][listenRevokedTokens] стартовал");

        try {
            tokenManagerService.onRevokedToken(record.value());

            log.info("[KafkaConsumerService][listenRevokedTokens] успешно отработал");
        } catch (Exception e) {
            log.error("[KafkaConsumerService][listenRevokedTokens] получена ошибка: {}", e.getMessage(), e);
        }
    }
}
