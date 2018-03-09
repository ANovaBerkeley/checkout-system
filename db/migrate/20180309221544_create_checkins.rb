class CreateCheckins < ActiveRecord::Migration[5.0]
  def change
    create_table :checkins do |t|
      t.boolean :is_mentor
      t.references :room, foreign_key: true
      t.references :mentor, foreign_key: true
      t.references :student, foreign_key: true

      t.timestamps
    end
  end
end
