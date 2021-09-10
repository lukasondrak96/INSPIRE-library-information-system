package com.lukasondrak.libraryinformationsystem.features.author;

import com.lukasondrak.libraryinformationsystem.features.item.Item;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Author implements Serializable {

    @Id
    @SequenceGenerator(name = "AuthorIdGenerator", sequenceName = "AUTHOR_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AuthorIdGenerator")
    private Long idAuthor;

    private String name;
    private String surname;

    @ManyToMany(mappedBy = "itemAuthors", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Item> authorItems = new ArrayList<>();

}
