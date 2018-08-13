package vn.hvbcvt.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.hvbcvt.core.entity.NewsEntity;
import vn.hvbcvt.dto.NewsDTO;

@Component
public class NewsConverter {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public NewsDTO convertToDto(NewsEntity entity) {
		NewsDTO result = modelMapper.map(entity, NewsDTO.class);
        return result;
    }

    public NewsEntity convertToEntity(NewsDTO dto) {
    	NewsEntity result = modelMapper.map(dto, NewsEntity.class);
        return result;
    }
}
