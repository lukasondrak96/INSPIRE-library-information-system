package com.lukasondrak.libraryinformationsystem.features.author;

import com.lukasondrak.libraryinformationsystem.features.item.Item;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Author implements Serializable {

    @Id
    @Setter(AccessLevel.PRIVATE)
    @SequenceGenerator(name = "AuthorIdGenerator", sequenceName = "AUTHOR_SEQUENCE", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AuthorIdGenerator")
    private Long idAuthor;

    @NotBlank(message = "Prosím vyplňte jméno autora")
    @Size(max = 255, message = "Příliš dlouhý vstup")
    private String name;

    @NotBlank(message = "Prosím vyplňte příjmení autora")
    @Size(max = 255, message = "Příliš dlouhý vstup")
    private String surname;

    @ManyToMany(mappedBy = "itemAuthors", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Item> authorItems = new ArrayList<>();

    public Author(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(idAuthor, author.idAuthor) && Objects.equals(name, author.name) && Objects.equals(surname, author.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAuthor, name, surname);
    }

    @Override
    public String toString() {
        return name + ' ' + surname;
    }
}
