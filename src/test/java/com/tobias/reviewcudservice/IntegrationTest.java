package com.tobias.reviewcudservice;

import com.tobias.reviewcudservice.ReviewCudServiceApp;
import com.tobias.reviewcudservice.config.AsyncSyncConfiguration;
import com.tobias.reviewcudservice.config.EmbeddedKafka;
import com.tobias.reviewcudservice.config.EmbeddedMongo;
import com.tobias.reviewcudservice.config.EmbeddedRedis;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { ReviewCudServiceApp.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedMongo
@EmbeddedKafka
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public @interface IntegrationTest {
}
