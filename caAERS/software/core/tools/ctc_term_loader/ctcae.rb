#!/usr/bin/env ruby

#
# Processes the CTCAEv3_M9 CSV into a bering migration
#

require 'csv'

$cat_order = []
$terms_by_cat = { }

def read()
  reader = CSV::Reader.create(File.open("CTCAEv3_M9.txt"))
  # skip first two rows
  reader.shift
  reader.shift
  
  id = 3001
  reader.each do |row|
    cat, term, select, ctep_term, ctep_code = row
    $cat_order << cat unless $cat_order.include? cat
    $terms_by_cat[cat] ||= [ ]
    $terms_by_cat[cat] << { 
      :term => term, :select_ae => select, :ctep_term => ctep_term, :ctep_code => ctep_code.to_i,
      :other_required => (term =~ /- Other/ ? '1' : nil), :id => id
    }
    id += 1
  end
end

def create()
  puts <<END
class AddCtcaeV3Terms extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
END

  puts "        // Have to break up the inserts so as not to exceed the java max method length"
  $cat_order.each_with_index do |cat, index|
    puts "        m#{index}()"
  end

  puts "    }"
  puts ""
  
  $cat_order.each_with_index do |cat, index|
    puts "    void m#{index}() {"
    print_inserts(cat, index)
    puts "    }"
    puts ""
  end

  puts <<END
    void down() {
        execute("DELETE FROM ctc_terms WHERE category_id > 300 AND category_id < 400")
        execute("DELETE FROM ctc_categories WHERE version_id=3")
    }
}
END
end

def print_inserts(cat, index)
  category_id = 300 + index + 1
  puts "        // #{cat} (#{$terms_by_cat[cat].size} terms)"
  puts "        insert('ctc_categories', [version_id: 3, id: #{category_id}, name: '#{cat}'], primaryKey: false)"
  $terms_by_cat[cat].each do |t|
    cols = ""
    [:id, :term, :select_ae, :ctep_term, :ctep_code, :other_required].each do |colname|
      value = t[colname]
      cols << "#{colname}: #{value.inspect}, " if value && value.size > 0
    end
    cols = cols.slice!(0, cols.size-2)
    puts "        insert('ctc_terms', [category_id: #{category_id}, #{cols}], primaryKey: false)"
  end
end

read()
create()
