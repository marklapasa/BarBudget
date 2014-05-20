package net.lapasa.barbudget;

import javax.inject.Singleton;

import net.lapasa.barbudget.dto.CategoryDTO;
import net.lapasa.barbudget.dto.EntryDTO;
import net.lapasa.barbudget.fragments.CategoryFormFragment;
import net.lapasa.barbudget.fragments.CategoryListFragment;
import net.lapasa.barbudget.fragments.EntryFormFragment;
import test.net.lapasa.barbduget.dto.CategoryDTOTest;
import dagger.Module;
import dagger.Provides;

@Module(
		injects={
				MainActivity.class, 
				CategoryDTOTest.class, 
				CategoryListFragment.class, 
				CategoryFormFragment.class,
				EntryFormFragment.class} )
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