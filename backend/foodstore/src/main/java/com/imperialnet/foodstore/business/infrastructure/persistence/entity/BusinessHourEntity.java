package com.imperialnet.foodstore.business.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "business_hours")
@Getter
@Setter
public class BusinessHourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dayOfWeek;
    private boolean enabled;

    @ElementCollection
    @CollectionTable(name = "business_hour_ranges", joinColumns = @JoinColumn(name = "business_hour_id"))
    private List<RangeEmbeddable> ranges;

    @Embeddable
    @Getter
    @Setter
    public static class RangeEmbeddable {
        @Column(name = "open_time")
        private String openTime;
        @Column(name = "close_time")
        private String closeTime;
    }
}
