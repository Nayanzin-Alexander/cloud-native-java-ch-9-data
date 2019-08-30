package com.nayanzin.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class User implements Serializable {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "last_name")
    private String lastName;
}
