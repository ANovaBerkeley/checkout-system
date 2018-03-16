class RenameMembersToStudents < ActiveRecord::Migration[5.0]
  def change
  	remove_reference :orders, :member, foreign_key: true
  	rename_table :members, :students
  	add_reference :orders, :student, foreign_key: true
  end
end
