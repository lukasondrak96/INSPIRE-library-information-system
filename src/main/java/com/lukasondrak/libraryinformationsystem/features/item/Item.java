package com.lukasondrak.libraryinformationsystem.features.item;

import com.lukasondrak.libraryinformationsystem.features.author.Author;
import com.lukasondrak.libraryinformationsystem.features.loanofitem.LoanOfItem;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
public class Item implements Serializable {

    @Id
    @Setter(AccessLevel.PRIVATE)
    @SequenceGenerator(name = "ItemIdGenerator", sequenceName = "ITEM_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ItemIdGenerator")
    private Long idItem;

    @NotBlank(message = "Prosím vyplňte název položky")
    @Size(max = 255, message = "Příliš dlouhý vstup")
    private String title;

    private int yearOfPublication;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Prosím vyplňte typ položky")
    @Size(max = 255, message = "Příliš dlouhý vstup")
    private ItemType type;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<LoanOfItem> loansOfItem = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "author_item_link",
            joinColumns = {@JoinColumn(name = "id_item")},
            inverseJoinColumns = {@JoinColumn(name = "id_author")}
    )
    private List<Author> itemAuthors = new ArrayList<>();
}
