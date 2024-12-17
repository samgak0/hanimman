package org.devkirby.hanimman.service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.devkirby.hanimman.dto.ShareTogetherDTO;

import jakarta.persistence.Query;

@RequiredArgsConstructor
@Service
public class ShareTogetherServiceImpl implements ShareTogetherService {
    private final String NATIVE_QUERY = """
SELECT 
    s.id, 
    s.title, 
    s.content, 
    s.created_at, 
    s.modified_at, 
    ad.neighborhood_name,
    s.location_date AS date_at, 
    s.quantity, 
    s.price, 
    s.user_id, 
    'share' AS `type`,
    si.image_id,
    COUNT(sf.id) OVER (PARTITION BY s.id) AS `favorite`,
    COUNT(sr.id) OVER (PARTITION BY s.id) AS `review`
FROM shares s
LEFT JOIN addresses ad ON ad.id = s.address_id
LEFT JOIN (
    SELECT 
        si.parent_id, 
        si.id AS image_id,
        ROW_NUMBER() OVER (PARTITION BY si.parent_id ORDER BY si.id) AS rn
    FROM share_images si
    WHERE si.deleted_at IS NULL
) si ON si.parent_id = s.id AND si.rn = 1 -- 첫 번째 이미지만 필터링
LEFT JOIN share_favorites sf ON sf.parent_id = s.id
LEFT JOIN share_reviews sr ON sr.parent_id = s.id
WHERE s.deleted_at IS NULL
AND s.is_end = 0

UNION ALL

SELECT 
    t.id, 
    t.content, 
    t.title, 
    t.created_at, 
    t.modified_at, 
    ad.neighborhood_name,
    t.meeting_at AS date_at, 
    t.quantity, 
    t.price, 
    t.user_id, 
    'together' AS `type`,
    ti.image_id,
    COUNT(tf.id) OVER (PARTITION BY t.id) AS `favorite`,
    COUNT(tp.id) OVER (PARTITION BY t.id) AS `participant`
FROM togethers t
LEFT JOIN addresses ad ON ad.id = t.address_id
LEFT JOIN (
    SELECT 
        ti.parent_id, 
        ti.id AS image_id,
        ROW_NUMBER() OVER (PARTITION BY ti.parent_id ORDER BY ti.id) AS rn
    FROM together_images ti
    WHERE ti.deleted_at IS NULL
) ti ON ti.parent_id = t.id AND ti.rn = 1
LEFT JOIN together_favorites tf ON tf.parent_id = t.id
LEFT JOIN together_participants tp ON tp.parent_id = t.id
WHERE t.deleted_at IS NULL
AND t.is_end = 0

ORDER BY created_at DESC
LIMIT 5;
            """;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<ShareTogetherDTO> getSharesAndTogethers() {
        Query query = entityManager.createNativeQuery(NATIVE_QUERY);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        List<ShareTogetherDTO> dtos = results.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return dtos;
    }

    private ShareTogetherDTO convertToDto(Object[] result) {
        return new ShareTogetherDTO(
                (Integer) result[0],                // id
                (String) result[1],                 // title
                (String) result[2],                 // content
                convertTimestamp(result[3]),        // createdAt
                convertTimestamp(result[4]),        // modifiedAt
                (String) result[5],                // address
                convertTimestamp(result[6]),        // dateAt
                (Integer) result[7],                // quantity
                (Integer) result[8],                 // price
                (Integer) result[9],                // userId
                (String) result[10],                 // type
                (Integer) result[11],                 // imageId
                (Long) result[12],                 // favorite
                (Long) result[13]                 // review
        );
    }

    private LocalDateTime convertTimestamp(Object timestampObj) {
        return timestampObj != null ? ((Timestamp) timestampObj).toLocalDateTime() : null;
    }
}
