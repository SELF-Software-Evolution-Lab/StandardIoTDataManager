package co.edu.uniandes.xrepo.repository;

import co.edu.uniandes.xrepo.domain.Tag;
import co.edu.uniandes.xrepo.domain.enumeration.TagType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


/**
 * Spring Data MongoDB repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    Page<Tag> findByType(Pageable pageable, TagType tagType);
}
