package net.lapasa.barbudget;

import javax.inject.Singleton;

import net.lapasa.barbudget.dto.CategoryDTO;
import net.lapasa.barbudget.dto.EntryDTO;
import net.lapasa.barbudget.fragments.CategoryListFragment;
import test.net.lapasa.barbduget.dto.CategoryDTOTest;
import test.net.lapasa.barbduget.dto.EntryDTOTest;
import dagger.Module;
import dagger.Provides;

@Module(injects={MainActivity.class, CategoryDTOTest.class, CategoryListFragment.class} )
public class BarBudgetModule
{
	@Provides
	@Singleton
	CategoryDTO provideCategoryDTO(EntryDTO entryDTO)
	{
		return new CategoryDTO(entryDTO);
	}

	@Provides
	@Singleton
	EntryDTO provideEntryDTO()
	{
		return new EntryDTO();
	}
}