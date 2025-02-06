package tw.com.ispan.init.pet;

import org.springframework.beans.factory.annotation.Autowired;

import tw.com.ispan.repository.pet.SpeciesRepository;

public class SaveSpeciesData {
	
	@Autowired
	private SpeciesRepository speciesRepository;
}
