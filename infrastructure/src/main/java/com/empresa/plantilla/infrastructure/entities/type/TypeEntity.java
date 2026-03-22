package com.empresa.plantilla.infrastructure.entities.type;

import com.empresa.plantilla.infrastructure.constants.EntitiesConstants;
import com.empresa.plantilla.infrastructure.entities.typeCategory.TypeCategoryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
		name = EntitiesConstants.TABLE_TYPE,
		uniqueConstraints = {
				@UniqueConstraint(
						name = EntitiesConstants.UQ_TYPE_CATEGORY_CODE_BY_CATEGORY,
						columnNames = {EntitiesConstants.COL_TYPE_CATEGORY_ID, EntitiesConstants.COL_CODE}
				)
		}
)
public class TypeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "typeSeqGenerator")
	@SequenceGenerator(name = "typeSeqGenerator", sequenceName = EntitiesConstants.SEQ_TYPE_ID, allocationSize = 1)
	@Column(name = EntitiesConstants.COL_ID, nullable = false)
	private Long id;

	@Column(name = EntitiesConstants.COL_TYPE_CATEGORY_ID, nullable = false)
	private Long typeCategoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = EntitiesConstants.COL_TYPE_CATEGORY_ID, insertable = false, updatable = false)
	private TypeCategoryEntity typeCategory;

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
}

