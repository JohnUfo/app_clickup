package uz.muydinovs.app_clickup.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.muydinovs.app_clickup.entity.template.AbsLongEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "workspace")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "owner_id"})})
public class Workspace extends AbsLongEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;

    @Column(nullable = false)
    private String initialLetter;

    @ManyToOne(fetch = FetchType.LAZY)
    private Attachment avatar;

    public Workspace(String name, String color, User owner, Attachment avatar) {
        this.name = name;
        this.color = color;
        this.owner = owner;
        this.avatar = avatar;
    }

    @PrePersist
    @PreUpdate
    public void setInitialLetterMyMethod() {
        this.initialLetter = name.substring(0, 1);
    }
}
