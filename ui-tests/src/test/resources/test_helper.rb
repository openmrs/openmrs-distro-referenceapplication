if ENV["COVERAGE"]
  require "simplecov"
  # start measuring the code coverage
  SimpleCov.start do
    # don't measure coverage of the tests themselves
    add_filter "/test/"
  end

  srcdir = File.expand_path("../src", __dir__)

  # track all files under src
  SimpleCov.track_files("#{srcdir}/**/*.java")

  # additionally use the LCOV format for on-line code coverage reporting at CI
  if ENV["CI"] || ENV["COVERAGE_LCOV"]
    require "simplecov-lcov"

    SimpleCov::Formatter::LcovFormatter.config do |c|
      c.report_with_single_file = true
      # this is the default Coveralls GitHub Action location
      # https://github.com/marketplace/actions/coveralls-github-action
      c.single_report_path = "coverage/lcov.info"
    end

    # generate both HTML and LCOV reports
    SimpleCov.formatter = SimpleCov::Formatter::MultiFormatter[
      SimpleCov::Formatter::HTMLFormatter,
      SimpleCov::Formatter::LcovFormatter
    ]
  end
end
