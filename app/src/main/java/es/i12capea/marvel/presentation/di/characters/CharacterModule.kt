package es.i12capea.marvel.presentation.di.characters

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import es.i12capea.marvel.data.api.CharacterApi
import es.i12capea.marvel.data.local.dao.CharacterDao
import es.i12capea.marvel.data.local.dao.CharacterShortDao
import es.i12capea.marvel.data.local.dao.QueryResultsDao
import es.i12capea.marvel.data.repository.CharacterRepositoryImpl
import es.i12capea.marvel.domain.repositories.CharacterRepository
import es.i12capea.marvel.domain.usecases.GetCharacterDetailUseCase
import es.i12capea.marvel.domain.usecases.GetCharactersWithOffsetUseCase

@Module
@InstallIn(ActivityRetainedComponent::class)
class CharacterModule {

    @Provides
    fun provideCharacterRepository(
        characterApi: CharacterApi,
        queryResultsDao: QueryResultsDao,
        characterShortDao: CharacterShortDao,
        characterDao: CharacterDao,
    ) : CharacterRepository{
        return CharacterRepositoryImpl(
            characterApi,
            queryResultsDao,
            characterShortDao,
            characterDao,
        )
    }

    @Provides
    fun provideGetCharactersWithOffsetUseCase(
        characterRepository: CharacterRepository
    ) : GetCharactersWithOffsetUseCase{
        return GetCharactersWithOffsetUseCase(
            characterRepository
        )
    }

    @Provides
    fun provideGetCharacterDetailUseCase(
        characterRepository: CharacterRepository
    ) : GetCharacterDetailUseCase{
        return GetCharacterDetailUseCase(
            characterRepository
        )
    }

}