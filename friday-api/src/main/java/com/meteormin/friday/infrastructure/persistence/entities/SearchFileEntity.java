package com.meteormin.friday.infrastructure.persistence.entities;

import com.meteormin.friday.infrastructure.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "search_file")
public class SearchFileEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "file_id")
    private FileEntity file;

    @ManyToOne
    private SearchEntity search;
}
