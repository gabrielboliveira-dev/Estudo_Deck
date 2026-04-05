package com.example.estudodeck.infrastructure;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.application.gateways.FlashcardSearchGateway;
import com.example.estudodeck.application.gateways.ReviewLogGateway;
import com.example.estudodeck.application.gateways.ReviewOutcomeGateway;
import com.example.estudodeck.application.usecases.*;
import com.example.estudodeck.domain.LevelingSystem;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataReviewLogRepository;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataReviewOutcomeRepository;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataUserRepository;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CreateDeckUseCase createDeckUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new CreateDeckUseCase(deckRepository, userContext);
    }

    @Bean
    public CreateFlashcardUseCase createFlashcardUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new CreateFlashcardUseCase(deckRepository, userContext);
    }

    @Bean
    public ReviewFlashcardUseCase reviewFlashcardUseCase(DeckRepository deckRepository, CreateReviewLogUseCase createReviewLogUseCase, CreateReviewOutcomeUseCase createReviewOutcomeUseCase, UserContext userContext, SpringDataUserRepository userRepository, LevelingSystem levelingSystem) {
        return new ReviewFlashcardUseCase(deckRepository, createReviewLogUseCase, createReviewOutcomeUseCase, userContext, userRepository, levelingSystem);
    }

    @Bean
    public DeleteDeckUseCase deleteDeckUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new DeleteDeckUseCase(deckRepository, userContext);
    }

    @Bean
    public DeleteFlashcardUseCase deleteFlashcardUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new DeleteFlashcardUseCase(deckRepository, userContext);
    }

    @Bean
    public UpdateDeckUseCase updateDeckUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new UpdateDeckUseCase(deckRepository, userContext);
    }

    @Bean
    public UnsuspendFlashcardUseCase unsuspendFlashcardUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new UnsuspendFlashcardUseCase(deckRepository, userContext);
    }

    @Bean
    public GetDeckHierarchyUseCase getDeckHierarchyUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new GetDeckHierarchyUseCase(deckRepository, userContext);
    }

    @Bean
    public GetReviewForecastUseCase getReviewForecastUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new GetReviewForecastUseCase(deckRepository, userContext);
    }

    @Bean
    public AddTagToDeckUseCase addTagToDeckUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new AddTagToDeckUseCase(deckRepository, userContext);
    }

    @Bean
    public RemoveTagFromDeckUseCase removeTagFromDeckUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new RemoveTagFromDeckUseCase(deckRepository, userContext);
    }

    @Bean
    public SearchFlashcardsUseCase searchFlashcardsUseCase(FlashcardSearchGateway searchGateway) {
        return new SearchFlashcardsUseCase(searchGateway);
    }

    @Bean
    public CreateReviewLogUseCase createReviewLogUseCase(ReviewLogGateway reviewLogGateway, UserContext userContext) {
        return new CreateReviewLogUseCase(reviewLogGateway, userContext);
    }

    @Bean
    public GetHeatmapDataUseCase getHeatmapDataUseCase(SpringDataReviewLogRepository reviewLogRepository, UserContext userContext) {
        return new GetHeatmapDataUseCase(reviewLogRepository, userContext);
    }

    @Bean
    public CreateReviewOutcomeUseCase createReviewOutcomeUseCase(ReviewOutcomeGateway reviewOutcomeGateway) {
        return new CreateReviewOutcomeUseCase(reviewOutcomeGateway);
    }

    @Bean
    public GetRetentionStatsUseCase getRetentionStatsUseCase(SpringDataReviewOutcomeRepository reviewOutcomeRepository, UserContext userContext) {
        return new GetRetentionStatsUseCase(reviewOutcomeRepository, userContext);
    }

    @Bean
    public ExportDeckToCsvUseCase exportDeckToCsvUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new ExportDeckToCsvUseCase(deckRepository, userContext);
    }

    @Bean
    public ImportDeckFromCsvUseCase importDeckFromCsvUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new ImportDeckFromCsvUseCase(deckRepository, userContext);
    }

    @Bean
    public GetCurrentStreakUseCase getCurrentStreakUseCase(ReviewLogGateway reviewLogGateway, UserContext userContext) {
        return new GetCurrentStreakUseCase(reviewLogGateway, userContext);
    }

    @Bean
    public PublishDeckUseCase publishDeckUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new PublishDeckUseCase(deckRepository, userContext);
    }

    @Bean
    public ListPublicDecksUseCase listPublicDecksUseCase(DeckRepository deckRepository) {
        return new ListPublicDecksUseCase(deckRepository);
    }

    @Bean
    public CloneDeckUseCase cloneDeckUseCase(DeckRepository deckRepository, UserContext userContext) {
        return new CloneDeckUseCase(deckRepository, userContext);
    }
}
