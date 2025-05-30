package com.yangdai.opennote.data.di

import android.content.Context
import androidx.room.Room
import com.yangdai.opennote.data.local.Database
import com.yangdai.opennote.data.local.MIGRATION_1_2
import com.yangdai.opennote.data.repository.AppDataStoreRepositoryImpl
import com.yangdai.opennote.data.repository.FolderRepositoryImpl
import com.yangdai.opennote.data.repository.NoteRepositoryImpl
import com.yangdai.opennote.data.repository.WidgetDataStoreRepositoryImpl
import com.yangdai.opennote.domain.usecase.AddFolder
import com.yangdai.opennote.domain.repository.NoteRepository
import com.yangdai.opennote.domain.usecase.AddNote
import com.yangdai.opennote.domain.usecase.DeleteFolder
import com.yangdai.opennote.domain.usecase.DeleteNote
import com.yangdai.opennote.domain.usecase.DeleteNotesByFolderId
import com.yangdai.opennote.domain.usecase.GetFolders
import com.yangdai.opennote.domain.usecase.GetNotes
import com.yangdai.opennote.domain.usecase.UseCases
import com.yangdai.opennote.domain.usecase.SearchNotes
import com.yangdai.opennote.domain.usecase.UpdateFolder
import com.yangdai.opennote.domain.usecase.UpdateNote
import com.yangdai.opennote.domain.repository.AppDataStoreRepository
import com.yangdai.opennote.domain.repository.FolderRepository
import com.yangdai.opennote.domain.repository.WidgetDataStoreRepository
import com.yangdai.opennote.domain.usecase.GetNoteById
import com.yangdai.opennote.domain.usecase.GetNotesCountByFolderId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDataStoreRepository(@ApplicationContext context: Context): AppDataStoreRepository =
        AppDataStoreRepositoryImpl(context)

    @Singleton
    @Provides
    fun provideWidgetDataStoreRepository(@ApplicationContext context: Context): WidgetDataStoreRepository =
        WidgetDataStoreRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): Database =
        Room.databaseBuilder(
            context,
            Database::class.java,
            Database.NAME
        ).addMigrations(MIGRATION_1_2).build()

    @Provides
    @Singleton
    fun provideNoteRepository(database: Database): NoteRepository =
        NoteRepositoryImpl(dao = database.noteDao)

    @Provides
    @Singleton
    fun provideFolderRepository(database: Database): FolderRepository =
        FolderRepositoryImpl(dao = database.folderDao)

    @Provides
    @Singleton
    fun provideNoteUseCases(
        noteRepository: NoteRepository,
        folderRepository: FolderRepository
    ): UseCases = UseCases(
        getNotes = GetNotes(noteRepository),
        getNoteById = GetNoteById(noteRepository),
        deleteNote = DeleteNote(noteRepository),
        addNote = AddNote(noteRepository),
        searchNotes = SearchNotes(noteRepository),
        updateNote = UpdateNote(noteRepository),
        deleteNotesByFolderId = DeleteNotesByFolderId(noteRepository),
        addFolder = AddFolder(folderRepository),
        updateFolder = UpdateFolder(folderRepository),
        deleteFolder = DeleteFolder(folderRepository),
        getFolders = GetFolders(folderRepository),
        getNotesCountByFolderId = GetNotesCountByFolderId(noteRepository)
    )
}
