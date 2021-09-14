package com.lukasondrak.libraryinformationsystem.features.item;

import com.lukasondrak.libraryinformationsystem.common.YearCustomConstraint;
import com.lukasondrak.libraryinformationsystem.features.author.Author;
import com.lukasondrak.libraryinformationsystem.features.loanofitem.LoanOfItem;
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
public class Item implements Serializable {

    @Id
    @Setter(AccessLevel.PRIVATE)
    @SequenceGenerator(name = "ItemIdGenerator", sequenceName = "ITEM_SEQUENCE", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ItemIdGenerator")
    private Long idItem;

    @NotBlank(message = "Prosím vyplňte název položky")
    @Size(max = 255, message = "Příliš dlouhý vstup")
    private String title;

    @YearCustomConstraint(message = "Nesprávný rok publikování")
    private int yearOfPublication;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    @OneToMany(mappedBy = "item", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<LoanOfItem> loansOfItem = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "author_item_link",
            joinColumns = {@JoinColumn(name = "id_item")},
            inverseJoinColumns = {@JoinColumn(name = "id_author")}
    )
    private List<Author> itemAuthors = new ArrayList<>();
}
