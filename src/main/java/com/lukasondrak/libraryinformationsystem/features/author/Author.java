package com.lukasondrak.libraryinformationsystem.features.author;

import com.lukasondrak.libraryinformationsystem.features.item.Item;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    @Setter(AccessLevel.PRIVATE)
    @SequenceGenerator(name = "AuthorIdGenerator", sequenceName = "AUTHOR_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AuthorIdGenerator")
    private Long idAuthor;

    @NotBlank(message = "Prosím vyplňte vaše jméno")
    @Size(max = 255, message = "Příliš dlouhý vstup")
    private String name;

    @NotBlank(message = "Prosím vyplňte vaše příjmení")
    @Size(max = 255, message = "Příliš dlouhý vstup")
    private String surname;

    @ManyToMany(mappedBy = "itemAuthors", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Item> authorItems = new ArrayList<>();

}
