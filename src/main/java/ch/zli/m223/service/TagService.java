package ch.zli.m223.service;

import ch.zli.m223.model.Tag;
import ch.zli.m223.repository.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TagService {

    @Inject
    TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.listAll();
    }

    public Tag findById(Long id) {
        return tagRepository.findById(id);
    }

    @Transactional
    public Tag createTag(Tag tag) {
        tagRepository.persist(tag);
        return tag;
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id);
        if (tag != null) {
            tagRepository.delete(tag);
        }
    }

    @Transactional
    public Tag updateTag(Long id, Tag updatedTag) {
        Tag tag = tagRepository.findById(id);

        if (tag == null) {
            return null;
        }

        tag.setTitle(updatedTag.getTitle());
        tagRepository.persist(tag);
        return tag;
    }
}
