package com.empresa.plantilla.infrastructure.entities.typeCategory;

import com.empresa.plantilla.infrastructure.constants.EntitiesConstants;
import com.empresa.plantilla.infrastructure.entities.type.TypeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = EntitiesConstants.TABLE_TYPE_CATEGORY,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = EntitiesConstants.UQ_TYPE_CATEGORY_CODE,
                        columnNames = {EntitiesConstants.COL_CODE}
                )
        }
)
public class TypeCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "typeCategorySeqGenerator")
    @SequenceGenerator(name = "typeCategorySeqGenerator", sequenceName = EntitiesConstants.SEQ_TYPE_CATEGORY_ID, allocationSize = 1)
    @Column(name = EntitiesConstants.COL_ID, nullable = false)
    private Long id;

    @Column(name = EntitiesConstants.COL_NAME, nullable = false, length = 150)
    private String name;

    @Column(name = EntitiesConstants.COL_CODE, nullable = false, length = 80)
    private String code;

    @Column(name = EntitiesConstants.COL_DESCRIPTION, length = 500)
    private String description;

    @Column(name = EntitiesConstants.COL_ACTIVE)
    private Boolean active;

    @Column(name = EntitiesConstants.COL_CREATE_BY, length = 80)
    private String createBy;

    @CreationTimestamp
    @Column(name = EntitiesConstants.COL_CREATE_DATE, updatable = false)
    private LocalDateTime createDate;

    @Column(name = EntitiesConstants.COL_UPDATE_BY, length = 80)
    private String updateBy;

    @UpdateTimestamp
    @Column(name = EntitiesConstants.COL_UPDATE_DATE)
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "typeCategory", fetch = FetchType.LAZY)
    private List<TypeEntity> types;
}

