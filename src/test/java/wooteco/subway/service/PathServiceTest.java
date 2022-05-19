package wooteco.subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql("/truncate.sql")
public class PathServiceTest {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final PathService pathService;

    @Autowired
    PathServiceTest(DataSource dataSource){
        stationDao = new StationDao(dataSource);
        sectionDao = new SectionDao(dataSource);
        lineDao = new LineDao(dataSource);
        pathService = new PathService(stationDao, sectionDao, lineDao);
    }

    @BeforeEach
    void setUp() {
        stationDao.insert(new Station("강남역"));
        stationDao.insert(new Station("선릉역"));
        stationDao.insert(new Station("잠실역"));
        lineDao.insert(new Line("2호선", "blue", 0));
        sectionDao.insert(new Section(1L, 1L, 2L, 10));
        sectionDao.insert(new Section(1L, 2L, 3L, 15));
    }

    @Test
    @DisplayName("경로를 탐색한다.")
    void searchPath() {
        PathResponse pathResponse = pathService.searchPath(new PathRequest(1L, 3L, 25));

        assertThat(pathResponse.getDistance()).isEqualTo(25);
        assertThat(pathResponse.getFare()).isEqualTo(1550);
    }
}
